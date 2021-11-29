package ru.gb.sbgb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.sbgb.config.ProductProps;
import ru.gb.sbgb.entity.Product;
import ru.gb.sbgb.repository.ProductRepository;
import ru.gb.sbgb.service.ProductService;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductProps productProps;
    private final ProductService productService;

    @ResponseBody
    @GetMapping
    public String helloWorld(){
        return "Hello Spring Boot";
    }

    @GetMapping("/hello")
    public String helloMessageJsp(Model model){
        model.addAttribute("msg", productProps.getProducts().size());
        return "hello";
    }


    // метод создания формы
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showSimpleForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "create-product";
    }

    // метод обработки формы и сохранения в бд
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String processForm(Product product) {
        productService.save(product);
        return "redirect:/product/all";
    }

    // метод вывода одного товара в браузер (product/3)
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getProductById(Model model,
                                 @PathVariable Integer id) {
        Product product;
        product = productService.findById(id);
        model.addAttribute("product", product);
        return "product";
    }

    // метод вывода списка товаров в браузер
    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product-list";
    }

    // метод удаления товара из хранилища
    @RequestMapping(path = "/delete", method = RequestMethod.GET)
    public String deleteById(@RequestParam Integer id) {
        productService.deleteById(id);
        return "redirect:/product/all";
    }

    // метод редактирования
    @RequestMapping(path = "/edit", method = RequestMethod.GET)
    public String editById(Model model, @RequestParam Integer id) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "create-product";
    }
}
