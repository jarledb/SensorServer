package no.stonehill.gui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GuiController {

    @RequestMapping("/")
    public String overview(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "overview";
    }

    @RequestMapping("/bs")
    public String bs(Model model) {
        return "bootstrap";
    }

    @RequestMapping("/temp")
    public String temp(Model model) {
        return "temp";
    }
}
