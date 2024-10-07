package com.embarkx.FirstSPring;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    /*@GetMapping("/hello/{name}")
    public HelloResponse helloParam(@PathVariable String name) {
        return new HelloResponse("Hello, " + name);
    } */

    @GetMapping("/hello")
    public HelloResponse hello() {
        return new HelloResponse(6);
    }

   /* @PostMapping("/hello")
    public HelloResponse helloPost(@RequestBody String name) {
        return new HelloResponse("Hello, " + name + "!");
    }*/

}
