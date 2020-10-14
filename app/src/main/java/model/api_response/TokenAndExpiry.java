package model.api_response;

public class TokenAndExpiry {
    private double expiration;
    public double getExpiration() { return expiration; }
    public void setExpiration(double expiration) { this.expiration = expiration; }

    private String token;
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
