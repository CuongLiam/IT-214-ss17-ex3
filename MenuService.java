package com.example.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class MenuService {

    // Giả lập Repository
    // private final MenuRepository menuRepository;

    /**
     * API Lấy dữ liệu
     * Lần đầu sẽ vào hàm, các lần sau lấy từ cache.
     */
    @Cacheable(value = "menuCache", key = "#id")
    public Menu getMenu(Long id) {
        System.out.println("[DB QUERY] -> Truy vấn DB để lấy menu ID: " + id);
        return new Menu(id, "Phở Bò", 55000); // Dữ liệu mẫu (giả lập lấy từ DB)
    }

    /**
     * API Update dữ liệu
     * Xóa cache sau khi cập nhật DB để đảm bảo dữ liệu nhất quán (Cache-Aside pattern).
     */
    @CacheEvict(value = "menuCache", key = "#menu.id")
    public Menu updateMenu(Menu menu) {
        System.out.println("[DB UPDATE] -> Đang cập nhật Database cho menu ID: " + menu.getId());
        
        // Bước 2: Thực hiện cập nhật database trước
        // menuRepository.save(menu);
        
        System.out.println("[DB UPDATE] -> Cập nhật DB thành công, tiến hành xóa cache tương ứng.");
        return menu;
    }

    public static class Menu {
        private Long id;
        private String dish_name;
        private double price;

        public Menu() {}

        public Menu(Long id, String dish_name, double price) {
            this.id = id;
            this.dish_name = dish_name;
            this.price = price;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getDish_name() { return dish_name; }
        public void setDish_name(String dish_name) { this.dish_name = dish_name; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }
}
