package com.layer;

/**
 * Your Layer app's configuration values.
 *
 * All values are available in the Layer Dashboard for your project.
 */
public class LayerConfig {
    private final static String LAYER_KEY_ID =
            "layer:///keys/e3b73796-3a1b-11e5-b8be-fdebe7056b10";
    private final static String LAYER_PROVIDER_ID =
            "layer:///providers/251c5b9c-cc41-11e4-9872-eae8a6002c37";
    private final static String LAYER_RSA_KEY_PATH =
            "/Users/sean/Desktop/layer.pk8";

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
