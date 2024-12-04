package app.finance.api.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import app.finance.api.Model.CategoryModel;

// ICategoryRepository interface
public interface ICategoryRepository extends JpaRepository<CategoryModel, Integer> {
    // Find category by user uid
    List<CategoryModel> findByUserUid(int uid);
}