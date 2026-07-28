package likelion14th.lte.statistic.entity;

import jakarta.persistence.*;
import likelion14th.lte.statistic.entity.StatWeek;
import likelion14th.lte.todo.entity.WeekEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@Table(name = "statistic")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int streak;

    @Column(nullable = false)
    private int monthPercent;

    @OneToMany(mappedBy = "statistic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StatWeek> statWeeks = new ArrayList<>();

    public static Statistic create() {
        Statistic statistic = new Statistic();
        statistic.streak = 0;
        statistic.monthPercent = 0;
        statistic.initializeWeeks();
        return statistic;
    }

    private void initializeWeeks() {
        for (WeekEnum week : WeekEnum.values()) {
            this.statWeeks.add(StatWeek.create(week, this));
        }
    }

    public WeekEnum getMostTodoWeek() {
        return statWeeks.stream()
                .max(Comparator.comparingInt(StatWeek::getCount))
                .map(likelion14th.lte.statistic.entity.StatWeek::getWeek)
                .orElse(null);
    }

    public void increaseStreakIfSuccess(boolean success) {
        if (success) {
            this.streak++;
        } else {
            this.streak = 0;
        }
    }

    public void updateMonthPercent(int percent) {
        this.monthPercent = percent;
    }
}