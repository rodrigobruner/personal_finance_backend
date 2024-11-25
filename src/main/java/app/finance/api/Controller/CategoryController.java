package app.finance.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.finance.api.Model.AccountModel;
import app.finance.api.Model.CategoryModel;
import app.finance.api.Model.CategoryType;
import app.finance.api.Repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // Create a new category
    @PostMapping
    public ResponseEntity<CategoryModel> createCategory(@RequestBody CategoryModel category) {
        category.setCategoryType(category.getCategoryType().toString().toUpperCase() == "EXPENSE" ? CategoryType.Expense : CategoryType.Income);
        CategoryModel savedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(savedCategory);
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryModel>> getAllCategories() {
        List<CategoryModel> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    // Get a category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryModel> getCategoryById(@PathVariable int id) {
        Optional<CategoryModel> category = categoryRepository.findById(id);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{uid}")
    public ResponseEntity<List<CategoryModel>> getCategoryByUid(@PathVariable("uid") int uid) {
            List<CategoryModel> categoryData = categoryRepository.findByUserUid(uid);
            if (!categoryData.isEmpty()) {
                return new ResponseEntity<>(categoryData, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

    // Update a category
    @PutMapping("/{id}")
    public ResponseEntity<CategoryModel> updateCategory(@PathVariable int id, @RequestBody CategoryModel categoryDetails) {
        Optional<CategoryModel> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            CategoryModel category = optionalCategory.get();
            category.setName(categoryDetails.getName());
            category.setCategoryType(categoryDetails.getCategoryType().toString().toUpperCase() == "EXPENSE" ? CategoryType.Expense : CategoryType.Income);

            category.setUser(categoryDetails.getUser());
            CategoryModel updatedCategory = categoryRepository.save(category);
            return ResponseEntity.ok(updatedCategory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        Optional<CategoryModel> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            categoryRepository.delete(optionalCategory.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}