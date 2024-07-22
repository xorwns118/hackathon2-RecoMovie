package com.hackathonteam2.recomovie.movie.service;

import com.hackathonteam2.recomovie.movie.dto.MovieResponseDto;
import com.hackathonteam2.recomovie.movie.dto.TMDBMovieResponseDto;
import com.hackathonteam2.recomovie.movie.entity.Movie;
import com.hackathonteam2.recomovie.movie.entity.MovieGenre;
import com.hackathonteam2.recomovie.movie.repository.GenreRepository;
import com.hackathonteam2.recomovie.movie.repository.MovieGenreRepository;
import com.hackathonteam2.recomovie.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieGenreRepository movieGenreRepository;

    public List<MovieResponseDto> search(String keyword) {
        return movieRepository.search(keyword).stream()
                .map(MovieResponseDto::of)
                .toList();
    }

    public MovieResponseDto findByMovieId(Long movieId) {
        return MovieResponseDto.of(movieRepository.findByMovieId(movieId));
    }

    @Transactional
    public MovieResponseDto addMovieFromTMDB(TMDBMovieResponseDto movieDto) {
        Movie movie = movieDto.toEntity();
        for(Long genreId : movieDto.getGenre_ids()) {
            movie.addGenre(genreRepository.findByGenreId(genreId));
        }
        for(MovieGenre mg : movie.getGenres()) {
            movieGenreRepository.save(mg);
        }
        return MovieResponseDto.of(movieRepository.save(movie));
    }
}
