package org.example.config;

import org.example.exceptions.CurrencyExchangeRateNotFoundException;
import org.example.exceptions.ScraperNotFoundException;
import org.example.utils.Error;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.lang.reflect.Parameter;
import java.util.*;

@ControllerAdvice(annotations = {RestController.class})
public class DefaultRestControllerAdviceConfiguration {
    private final MessageSource messageSource;

    public DefaultRestControllerAdviceConfiguration(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(value = {CurrencyExchangeRateNotFoundException.class})
    public ResponseEntity<?> handleCurrencyExchangeRateNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = {ScraperNotFoundException.class})
    public ResponseEntity<?> handleScraperNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Error> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Map<String, List<String>> errors = new HashMap<>();
        List<String> messages = new ArrayList<>(1);
        messages.add(
                this.messageSource.getMessage(
                        "validation.invalidType", new Object[]{}, LocaleContextHolder.getLocale()
                )
        );

        Parameter parameter = e.getParameter().getParameter();
        String name = parameter.getName();

        if (parameter.isAnnotationPresent(RequestParam.class)) {
            RequestParam annotation = parameter.getAnnotation(RequestParam.class);

            if (!annotation.name().isEmpty()) {
                name = annotation.name();
            }
        }

        errors.put(name, messages);

        return ResponseEntity.badRequest().body(new Error(errors));
    }

    @ExceptionHandler(value = HandlerMethodValidationException.class)
    public ResponseEntity<Error> handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        Map<String, List<String>> errors = new HashMap<>(e.getAllValidationResults().size());

        for (ParameterValidationResult parameterValidationResult : e.getAllValidationResults()) {
            Parameter parameter = parameterValidationResult.getMethodParameter().getParameter();
            String name = parameter.getName();

            if (parameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam annotation = parameter.getAnnotation(RequestParam.class);

                if (!annotation.name().isEmpty()) {
                    name = annotation.name();
                }
            }

            errors.put(
                    name,
                    parameterValidationResult
                            .getResolvableErrors()
                            .stream()
                            .map(
                                    (MessageSourceResolvable messageSourceResolvable) ->
                                            Objects.requireNonNullElse(messageSourceResolvable.getDefaultMessage(), "")
                            )
                            .toList()
            );
        }

        return ResponseEntity.badRequest().body(new Error(errors));
    }
}