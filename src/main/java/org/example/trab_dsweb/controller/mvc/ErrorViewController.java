package org.example.trab_dsweb.controller.mvc;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;
import java.util.Map;

@Component
public class ErrorViewController implements ErrorViewResolver {
  private final MessageSource messageSource;

  public ErrorViewController(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> map) {
    ModelAndView model = new ModelAndView("error");
    model.addObject("status", status.value());
    Locale locale = request.getLocale();

    switch (status.value()) {
      case 400:
        String error400 = messageSource.getMessage("error.type.400", null, locale);
        model.addObject("error", error400);
        break;
      case 401:
        String error401 = messageSource.getMessage("error.type.401", null, locale);
        model.addObject("error", error401);
        break;
      case 403:
        String error403 = messageSource.getMessage("error.type.403", null, locale);
        model.addObject("error", error403);
        break;
      case 404:
        String error404 = messageSource.getMessage("error.type.404", null, locale);
        model.addObject("error", error404);
        break;
      case 409:
        String error409 = messageSource.getMessage("error.type.409", null, locale);
        model.addObject("error", error409);
        break;
      case 500:
        String error500 = messageSource.getMessage("error.type.500", null, locale);
        model.addObject("error", error500);
        break;
      default:
        model.addObject("error", map.get("error"));
        break;
    }
    String message = map.get("message").toString();
    model.addObject("message", message);
    return model;
  }
}
