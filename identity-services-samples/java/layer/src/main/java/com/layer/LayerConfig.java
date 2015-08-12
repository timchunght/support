package com.layer;

/**
 * Your Layer app's configuration values.
 *
 * All values are available in the Layer Dashboard for your project.
 */
public class LayerConfig {
    private final static String LAYER_KEY_ID = "";
    private final static String LAYER_PROVIDER_ID = "";
    private final static String LAYER_RSA_KEY_PATH = "";

    public String getProviderId() {
        return LAYER_PROVIDER_ID;
    }

    public String getLayerKeyId() {
        return LAYER_KEY_ID;
    }

    public String getRsaKeyPath() {
        return LAYER_RSA_KEY_PATH;
    }
}
