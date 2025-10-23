
package com.livecommerce.auth.api;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import com.livecommerce.auth.jwt.JwtService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class Controller { // role from request for demo
  private final JwtService jwt;

  @PostMapping("/login")
  public Map<String,Object> login(@RequestBody Map<String,String> body){
    String sub = body.getOrDefault("mobile_number","user:demo");
    String role = body.getOrDefault("role","CUSTOMER");
    String access = jwt.createAccessToken(sub, Map.of("role",role), 3600);
    String refresh = jwt.createAccessToken(sub, Map.of("type","refresh"), 86400);
    return Map.of("access_token", access, "refresh_token", refresh);
  }
  @PostMapping("/refresh")
  public Map<String,Object> refresh(@RequestBody Map<String,String> body){
    String sub = "user:demo";
    String role = body.getOrDefault("role","CUSTOMER");
    String access = jwt.createAccessToken(sub, Map.of("role",role), 3600);
    return Map.of("access_token", access);
  }
  @GetMapping("/me")
  public Map<String,Object> me(){ return Map.of("user","demo"); }
}
