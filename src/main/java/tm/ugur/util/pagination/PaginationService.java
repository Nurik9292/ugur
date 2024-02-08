package tm.ugur.util.pagination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaginationService {

    public <T> Page<T> createPage(List<T> content, int pageNumber, int itemsPerPage) {
        int totalPages = (int) Math.ceil((double) content.size() / itemsPerPage);

            pageNumber = Math.min(Math.max(pageNumber, 1), totalPages);

        int startItem = (pageNumber - 1) * itemsPerPage;
        List<T> pageContent = content.subList(startItem, Math.min(startItem + itemsPerPage, content.size()));

        return new PageImpl<>(pageContent, PageRequest.of(pageNumber - 1, itemsPerPage), content.size());
    }
}
