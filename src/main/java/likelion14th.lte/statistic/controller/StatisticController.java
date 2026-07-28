package likelion14th.lte.statistic.controller;

import likelion14th.lte.global.api.ApiResponse;
import likelion14th.lte.global.api.SuccessCode;
import likelion14th.lte.statistic.dto.response.StatisticResponse;
import likelion14th.lte.statistic.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping
    public ApiResponse<StatisticResponse> getStatistic(@RequestParam Long userId) {
        return ApiResponse.onSuccess(SuccessCode.STATISTICS_GET_SUCCESS, statisticService.getStatistic(userId));
    }
}