package com.guessinggamemvc.controller;

import com.guessinggamemvc.component.Game;
import com.guessinggamemvc.component.GameRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Controller for the Guessing Game MVC API
 */
@RequiredArgsConstructor
@Controller
public class GuessingGameMvcController {
    @NonNull
    private final GameRepository gameRepository;
    private WebClient client =
            WebClient.create("http://localhost:8082/guess/games");

    /**
     * GET database records of all games currently being played.
     * @return Iterable list of Game records.
     */
    @GetMapping("/games")
    public String getAllGames(Model model) {
        gameRepository.deleteAll();

        client.get()
                .retrieve()
                .bodyToFlux(Game.class)
                .toStream()
                .forEach(gameRepository::save);

        model.addAttribute("currentGames", gameRepository.findAll());
        return "games"; // name of a template (sans .html extension)
    }
}
