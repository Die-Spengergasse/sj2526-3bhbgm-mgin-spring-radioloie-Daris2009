package at.spengergasse.spring_thymeleaf.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
    List<Reservation> findByDeviceIdOrderByStartTimeAsc(int deviceId);

    @Query("select r from Reservation r where r.device.id = :deviceId and r.startTime < :end and r.endTime > :start")
    List<Reservation> findOverlapping(@Param("deviceId") int deviceId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
