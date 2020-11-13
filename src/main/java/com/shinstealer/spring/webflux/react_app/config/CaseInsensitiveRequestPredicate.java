package com.shinstealer.spring.webflux.react_app.config;

import java.net.URI;
import org.springframework.http.server.PathContainer;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.support.ServerRequestWrapper;

public class CaseInsensitiveRequestPredicate implements RequestPredicate {

  private RequestPredicate target;

  public CaseInsensitiveRequestPredicate(RequestPredicate target) {
    this.target = target;
  }

  @Override
  public boolean test(ServerRequest req) {

    return this.target.test(new LowerCaseUriServerRequestWrapper(req));
  }

  @Override
  public String toString() {

    return this.target.toString();
  }

  public class LowerCaseUriServerRequestWrapper extends ServerRequestWrapper {

    public LowerCaseUriServerRequestWrapper(ServerRequest delegate) {
      super(delegate);

    }

    @Override
    public URI uri() {

      return URI.create(super.uri().toString().toLowerCase());
    }

    @Override
    public String path() {

      return uri().getPath();
    }

    @Override
    public PathContainer pathContainer() {

      return PathContainer.parsePath(path());
    }


  }

}
