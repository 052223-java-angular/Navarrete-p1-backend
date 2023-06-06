package com.revature.movietn.entities;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    private String id;

    @Column(name = "total_rating", nullable = false)
    private BigDecimal totalRating;

    @Column(name = "total_votes", nullable = false)
    private int totalVotes;

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Review> reviews;

    @ManyToMany(mappedBy = "movies", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "movies")
    private Set<MovieList> movieLists;

    public Movie(BigDecimal totalRating, int totalVotes) {
        this.id = UUID.randomUUID().toString();
        this.totalRating = totalRating;
        this.totalVotes = totalVotes;
        this.reviews = new HashSet<>();
        this.movieLists = new HashSet<>();
    }
}