package app.finance.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.finance.api.Model.AccountModel;
import app.finance.api.Model.CategoryModel;
import app.finance.api.Model.CategoryType;
import app.finance.api.Repository.ICategoryRepository;

import java.util.List;
import java.util.Optional;

// Category for the category controller
@RestController
@RequestMapping("/Categories") // API route
@CrossOrigin(origins = "*") // Allow requests from any origin
public class CategoryController {

    // dependency injection for the repositories
    @Autowired
    private ICategoryRepository categoryRepository;

    // Create a new category
    @PostMapping
    public ResponseEntity<CategoryModel> createCategory(@RequestBody CategoryModel category) {
        // Set the category type
        CategoryType categoryType = category.getCategoryType().toString().toUpperCase().equals("EXPENSE") ? 
            CategoryType.Expense : CategoryType.Income;
        // Set the category type
        category.setCategoryType(categoryType);
        // Save the category
        CategoryModel savedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(savedCategory);
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryModel>> getAllCategories() {
        // Get all categories
        List<CategoryModel> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    // Get a category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryModel> getCategoryById(@PathVariable int id) {
        // Get a category by ID
        Optional<CategoryModel> category = categoryRepository.findById(id);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get a category by user ID
    @GetMapping("/user/{uid}")
    public ResponseEntity<List<CategoryModel>> getCategoryByUid(@PathVariable("uid") int uid) {
        // Get a category by user ID
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
        // Get a category by ID
        Optional<CategoryModel> optionalCategory = categoryRepository.findById(id);
        // Check if the category exists
        if (optionalCategory.isPresent()) {
            // Get the category
            CategoryModel category = optionalCategory.get();
            // Set the category details
            category.setName(categoryDetails.getName());
            // Set the category type
            CategoryType categoryType = categoryDetails.getCategoryType().toString().toUpperCase().equals("EXPENSE") ? 
            CategoryType.Expense : CategoryType.Income;
            // Set the category type
            category.setCategoryType(categoryType);
            // Set the user
            category.setUser(categoryDetails.getUser());
            // Save the category
            CategoryModel updatedCategory = categoryRepository.save(category);
            return ResponseEntity.ok(updatedCategory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        // Get a category by ID
        Optional<CategoryModel> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) { // if the category exists and delete it
            categoryRepository.delete(optionalCategory.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}