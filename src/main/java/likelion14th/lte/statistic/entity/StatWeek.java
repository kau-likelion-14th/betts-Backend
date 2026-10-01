package likelion14th.lte.statistic.entity;

import jakarta.persistence.*;
import likelion14th.lte.todo.entity.WeekEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "stat_week")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StatWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WeekEnum week;

    @Column(nullable = false)
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statistic_id")
    private likelion14th.lte.statistic.entity.Statistic statistic;

    public static StatWeek create(WeekEnum week, likelion14th.lte.statistic.entity.Statistic statistic) {
        StatWeek statWeek = new StatWeek();
        statWeek.week = week;
        statWeek.count = 0;
        statWeek.statistic = statistic;
        return statWeek;
    }

    public void increaseCount() {
        this.count++;
    }
}