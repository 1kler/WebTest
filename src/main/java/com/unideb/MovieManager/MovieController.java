package com.unideb.MovieManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MovieController {
    private final MovieRepository movieRepository;
    @Autowired
    private MovieService movieService;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/movies/new")
    public String createMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "movies/create";
    }

    @PostMapping("/movies/new")
    public String createMovie(Movie movie) {
        movieService.saveMovie(movie);
        return "redirect:/movies/list";
    }

    @GetMapping("/movies/{id}")
    public String viewMovie(@PathVariable Long id, @RequestParam(value = "rating", required = false) Integer rating, Model model) {
        Movie movie = movieService.getMovieById(id);
        if (movie == null) {
            return "redirect:/movies/list";
        }
        model.addAttribute("movie", movie);
        model.addAttribute("selectedRating", rating);
        return "movies/details";
    }

    @GetMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return "redirect:/movies/list";
    }

    @PostMapping("/movies/updateRating/{id}")
    public String updateRating(@PathVariable Long id, @RequestParam("rating") int rating, Model model) {
        Movie movie = movieService.getMovieById(id);
        if (movie == null) {
            return "redirect:/";
        }

        movie.setRating(rating);
        movieService.saveMovie(movie);

        return "redirect:/movies/" + id;
    }

    @GetMapping("/movies/list")
    public String listMovies(@RequestParam(value = "rating", required = false) Integer rating, Model model) {
        if (rating != null) {
            model.addAttribute("movies", movieService.getMoviesByRating(rating));
            model.addAttribute("selectedRating", rating);  // Pass the selected rating to the list page
        } else {
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("selectedRating", null);  // No selected filter
        }
        return "movies/list";  // Return the movie list page
    }

}
