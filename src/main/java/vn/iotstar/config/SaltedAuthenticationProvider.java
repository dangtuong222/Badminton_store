package vn.iotstar.config;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.PasswordService;

@Component
public class SaltedAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials() != null ? authentication.getCredentials().toString() : "";

        Optional<User> userOpt = userRepository.findByEmail(username);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByPhone(username);
        }
        if (userOpt.isEmpty()) {
            throw new BadCredentialsException("Thông tin đăng nhập không hợp lệ");
        }

        User user = userOpt.get();

        if (user.getStatus() == User.UserStatus.BANNED) {
            throw new LockedException("Tài khoản đã bị khóa");
        }
        if (user.getStatus() != User.UserStatus.ACTIVE) {
            throw new DisabledException("Tài khoản chưa được kích hoạt");
        }

        boolean matches = passwordService.verifyPassword(rawPassword, user.getHashedPassword(), user.getSalt());
        if (!matches) {
            throw new BadCredentialsException("Thông tin đăng nhập không hợp lệ");
        }

        String role = user.getRole() != null ? user.getRole().name() : "USER";
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

        return new UsernamePasswordAuthenticationToken(user.getEmail(), user.getHashedPassword(), authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
