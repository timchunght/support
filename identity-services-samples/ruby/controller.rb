# NOTE: Assumes you're using Devise for authentication.

class SessionsController < Devise::SessionsController  
  def create
    # NOTE: We disable the session storage for XML/JSON requests. API access is handled purely by Token headers
    self.resource = warden.authenticate!(auth_options.merge(store: !sessionless_request?))
    set_flash_message(:notice, :signed_in) if is_navigational_format?
    sign_in(resource_name, resource)
    yield resource if block_given?
    respond_with(resource) do |format|
      format.html { redirect_to(after_sign_in_path_for(resource)) }
      format.js { render nothing: true, status: :created }
      format.json do
        @identity_token = IdentityToken.new(user_id: resource.id, 
                                            nonce: params[:nonce], 
                                            expires_at: 2.weeks.from_now) if params[:nonce]
        json = Jbuilder.encode do |json|
          json.authentication_token resource.authentication_token
          json.layer_identity_token @identity_token.to_s
          json.user resource.to_builder
        end                       
        render json: json, status: :created
      end
    end
  end

  def sessionless_request?
    request.format.xml? || request.format.json?
  end
end