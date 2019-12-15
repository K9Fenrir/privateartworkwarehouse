package si.fri.paw.entities;

import javax.json.bind.annotation.JsonbTransient;
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
        private String username;

        private String email;

        @JsonbTransient
        private String passHash;

        private boolean admin;

        @OneToMany(mappedBy = "author")
        private Set<Post> uploads;

        @ManyToMany(mappedBy = "favouritedBy")
        private Set<Post> favourites;

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

    public String getPassHash() {
        return passHash;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }

    public boolean isAdmin() {
                return admin;
        }

        public void setAdmin(boolean admin) {
                this.admin = admin;
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
