package sample.coffeekiosk.unit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import sample.coffeekiosk.unit.Beverage.Beverage;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
@ToString
public class Order {

    private final LocalDateTime orderDateTime;
    private final List<Beverage> beverages;

}
