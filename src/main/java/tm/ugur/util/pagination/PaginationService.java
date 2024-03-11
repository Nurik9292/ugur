package tm.ugur.util.pagination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.thymeleaf.expression.Numbers;

import java.util.List;
import java.util.Locale;

@Component
public class PaginationService {



    public <T> Page<T> createPage(List<T> content, int pageNumber, int itemsPerPage) {
        int totalPages = (int) Math.ceil((double) content.size() / itemsPerPage);

            pageNumber = Math.min(Math.max(pageNumber, 1), totalPages);

        int startItem = (pageNumber - 1) * itemsPerPage;
        List<T> pageContent = content.subList(startItem, Math.min(startItem + itemsPerPage, content.size()));

        return new PageImpl<>(pageContent, PageRequest.of(pageNumber - 1, itemsPerPage), content.size());
    }



    public Integer[] getTotalPage(int totalPages, int currentPage){
        Numbers numbers = new Numbers(Locale.getDefault());
        return numbers.sequence(currentPage > 4 ? currentPage - 1 : 1, currentPage + 4 < totalPages ? currentPage + 3 : totalPages);
    }
}
