package app.finance.api.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import app.finance.api.Model.CategoryModel;

public interface CategoryRepository extends JpaRepository<CategoryModel, Integer> {
    List<CategoryModel> findByUserUid(int uid);
}