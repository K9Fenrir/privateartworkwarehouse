package si.fri.paw.entities;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "users")
@NamedQueries(value =
        {
                @NamedQuery(name = "User.getAll", query = "SELECT u FROM users u"),
                @NamedQuery(name = "User.getByUsername", query = "SELECT u FROM users u WHERE u.username = :username"),
                @NamedQuery(name = "User.getByEmail", query = "SELECT u FROM users u WHERE u.email = :email"),
        })

public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        private String username;

        private String email;

        @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST)
        private Set<Post> uploads;

        @ManyToMany(mappedBy = "favouritedBy", cascade = CascadeType.PERSIST)
        private Set<Post> favourites;

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public Set<Post> getUploads() {
                return uploads;
        }

        public void setUploads(Set<Post> uploads) {
                this.uploads = uploads;
        }

        public Set<Post> getFavourites() {
                return favourites;
        }

        public void setFavourites(Set<Post> favourites) {
                this.favourites = favourites;
        }
}
