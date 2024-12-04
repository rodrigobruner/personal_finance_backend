package app.finance.api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.finance.api.Model.UserModel;

// IUserRepository interface
public interface IUserRepository extends JpaRepository<UserModel, Integer>{
    // Find user by email
    UserModel findByEmail(String email);
}