package securatta.domain

/**
 * Represents a user. A user is an authenticated individual.
 *
 * @since 0.1.0
 */
class User {

    /**
     * Human friendly name
     *
     * @since 0.1.0
     */
    String name

    /**
     * User username, should be unique within the system
     *
     * @since 0.1.0
     */
    String username
}
