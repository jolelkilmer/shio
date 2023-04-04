package com.viglet.shio.provider.auth;

import com.viglet.shio.persistence.model.auth.ShUser;
import org.springframework.security.authentication.AuthenticationProvider;

public interface ShAuthenticationProvider extends AuthenticationProvider {

  public void init(String providerId);

  public ShUser getShUser(String username);
}
