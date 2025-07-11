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
    Locale locale = request.getLocale();

    model.addObject("status", status.value());

    switch (status.value()) {
      case 400:
        String error400 = messageSource.getMessage("error.type.400", null, locale);
        String message400 = messageSource.getMessage("error.type.400.detail", new Object[]{map.get("path")}, locale);
        model.addObject("error", error400);
        model.addObject("message", message400);
        break;
      case 404:
        String error404 = messageSource.getMessage("error.type.404", null, locale);
        String message404 = messageSource.getMessage("error.type.404.detail", new Object[]{map.get("path")}, locale);
        model.addObject("error", error404);
        model.addObject("message", message404);
        break;
      case 409:
        String error409 = messageSource.getMessage("error.type.409", null, locale);
        String message409 = messageSource.getMessage("error.type.409.detail", new Object[]{map.get("path")}, locale);
        model.addObject("error", error409);
        model.addObject("message", message409);
        break;
      case 500:
        String error500 = messageSource.getMessage("error.type.500", null, locale);
        String message500 = messageSource.getMessage("error.type.500.detail", null, locale);
        model.addObject("error", error500);
        model.addObject("message", message500);
        break;
      default:
        model.addObject("error", map.get("error"));
        model.addObject("message", map.get("message"));
        break;
    }
    return model;
  }
}
