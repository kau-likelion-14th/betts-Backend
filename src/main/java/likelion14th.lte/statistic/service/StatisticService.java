package likelion14th.lte.statistic.service;

import jakarta.persistence.EntityManager;
import likelion14th.lte.global.api.ErrorCode;
import likelion14th.lte.global.exception.GeneralException;
import likelion14th.lte.statistic.dto.response.StatisticResponse;
import likelion14th.lte.statistic.entity.Statistic;
import likelion14th.lte.statistic.entity.StatWeek;
import likelion14th.lte.todo.repository.TodoDateRepository;
import likelion14th.lte.user.entity.User;
import likelion14th.lte.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class StatisticService {

    private final UserRepository userRepository;
    private final TodoDateRepository todoDateRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public StatisticResponse getStatistic(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));
        return StatisticResponse.from(user.getStatistic());
    }

    private void updateStatistic(User user) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Statistic statistic = user.getStatistic();

        boolean hasCompleted = todoDateRepository
                .existsByTodo_User_IdAndDateAndCompleted(user.getId(), yesterday, true);
        boolean hasIncomplete = todoDateRepository
                .existsByTodo_User_IdAndDateAndCompleted(user.getId(), yesterday, false);

        boolean isSuccess = hasCompleted && !hasIncomplete;
        statistic.increaseStreakIfSuccess(isSuccess);

        if (isSuccess) {
            StatWeek targetWeek = statistic.getStatWeeks().stream()
                    .filter(w -> w.getWeek().toDayOfWeek() == yesterday.getDayOfWeek())
                    .findFirst()
                    .orElseThrow(() -> new GeneralException(ErrorCode.STAT_WEEK_NOT_FOUND));
            // 해당 요일 StatWeek가 없으면 즉시 예외 발생

            targetWeek.increaseCount();
        }

        LocalDate thirtyDaysAgo = yesterday.minusDays(30);
        long completed = todoDateRepository
                .countByTodo_User_IdAndDateBetweenAndCompleted(user.getId(), thirtyDaysAgo, yesterday, true);
        long incomplete = todoDateRepository
                .countByTodo_User_IdAndDateBetweenAndCompleted(user.getId(), thirtyDaysAgo, yesterday, false);
        long total = completed + incomplete;

        int percent = total == 0 ? 0 : (int) (completed * 100 / total);
        statistic.updateMonthPercent(percent);
    }

    @Transactional
    public void updateAllStatistics() {
        int page = 0;
        int size = 500;
        Page<User> userPage;

        do {
            userPage = userRepository.findAll(PageRequest.of(page, size));
            for (User user : userPage.getContent()) {
                updateStatistic(user);
            }
            entityManager.flush();
            entityManager.clear();
            page++;
        } while (userPage.hasNext());
    }
}