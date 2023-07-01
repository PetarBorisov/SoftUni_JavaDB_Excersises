package entity;

import javax.persistence.*;

@Entity
@Table(name = "credit_cards")
public class CreditCard extends BillingDetail{

    private CardType cardType;
    private Integer expirationMOnth;
    private Integer expirationYear;

    public CreditCard() {
    }
@Enumerated(EnumType.STRING)
    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }
@Column(name = "exp_month")
    public Integer getExpirationMOnth() {
        return expirationMOnth;
    }

    public void setExpirationMOnth(Integer expirationMOnth) {
        this.expirationMOnth = expirationMOnth;
    }
@Column(name = "exp_year")
    public Integer getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
    }
}
