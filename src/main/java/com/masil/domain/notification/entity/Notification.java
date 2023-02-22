package com.masil.domain.notification.entity;

import com.masil.domain.member.entity.Member;
import com.masil.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @Column(nullable = false)
    private String url;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isRead = false;

    @Column(nullable = false)
    private String content;

    @Builder
    public Notification(Member sender, Member receiver, NotificationType notificationType, String url, Boolean isRead, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.url = url;
        this.isRead = isRead;
        this.content = content;
    }

    public void read() {
        this.isRead = true;
    }
}
