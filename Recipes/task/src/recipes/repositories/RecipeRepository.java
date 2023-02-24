package recipes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import recipes.models.Recipe;

import java.util.List;


@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query(value="from Recipe t where lower(t.name) like lower(:name) order by t.date desc ")
    List<Recipe> searchByName(@Param("name") String name);

    @Query(value="from Recipe t where lower(t.category) = lower(:category) order by t.date desc ")
    List<Recipe> searchByCategory(@Param("category") String category);


}