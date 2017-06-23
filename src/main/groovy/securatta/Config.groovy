package securatta

/**
 * @since 0.1.0
 */
class Config {
    /**
     * @since 0.1.0
     */
    Data data

    /**
     * @since 0.1.0
     */
    Security security

    /**
     * @since 0.1.0
     */
    static class Data {
        /**
         * @since 0.1.2
         */
        String contactPoint

        /**
         * @since 0.1.0
         */
        String username

        /**
         * @since 0.1.0
         */
        String password
    }

    /**
     * @since 0.1.0
     */
    static class Security {
        /**
         * @since 0.1.0
         */
        String secret

        /**
         * @since 0.1.0
         */
        String issuer
    }
}
