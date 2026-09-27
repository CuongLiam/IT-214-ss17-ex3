package com.example.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    // Xem thông tin menu
    @GetMapping("/{id}")
    public MenuService.Menu getMenu(@PathVariable Long id) {
        return menuService.getMenu(id);
    }

    // Cập nhật giá món ăn
    @PutMapping
    public MenuService.Menu updateMenu(@RequestBody MenuService.Menu menu) {
        return menuService.updateMenu(menu);
    }
}
