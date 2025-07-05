package saeg.ecommerceback.model;

public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    MODERATOR("ROLE_MODERATOR");
    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
