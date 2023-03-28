package com.boardadmin.project.controller;

import com.boardadmin.project.domain.repository.AdminAccountRepository;
import com.boardadmin.project.dto.response.AdminAccountResponse;
import com.boardadmin.project.service.AdminAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin/members")
@Controller
public class AdminAccountController {

    private final AdminAccountService adminAccountService;

    @GetMapping
    public String members(Model model) {
        return "admin/members";
    }

    @ResponseBody
    @GetMapping("/api/admin/members")
    public List<AdminAccountResponse> getMembers() {
        return List.of();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    @DeleteMapping("/api/admin/members/{userId}")
    public void delete(@PathVariable String userId) {
    }

}
