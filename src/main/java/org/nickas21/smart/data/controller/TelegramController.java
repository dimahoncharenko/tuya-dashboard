package org.nickas21.smart.data.controller;

import org.nickas21.smart.data.service.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api")
public class TelegramController {

    @Autowired
    private TelegramService telegramService;


    @PostMapping("/sendNotification")
    public Mono<ResponseEntity<String>> sendNotification(@RequestBody String message) {
        return telegramService.sendNotification(message)
                .flatMap(isSentMessage -> {
                    if (isSentMessage) {
                        return Mono.just(ResponseEntity.ok("Send Notification is successful"));
                    } else {
                        return Mono.error(new Throwable("Invalid send Notification"));
                    }
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(e.getMessage() == null ? "Invalid password or username" : e.getMessage())));
    }
}
