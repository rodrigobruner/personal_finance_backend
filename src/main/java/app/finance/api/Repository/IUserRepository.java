package app.finance.api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.finance.api.Model.UserModel;

public interface IUserRepository extends JpaRepository<UserModel, Integer>{
    UserModel findByEmail(String email);
}