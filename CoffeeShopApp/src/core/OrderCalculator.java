package core;

public class OrderCalculator {
    private static final double COMBO_DISCOUNT_RATE = 0.2;
    private static final double BULK_DISCOUNT_RATE = 0.1;

    public double calculateDiscount(Order order){
        double orderPrice = order.getTotalCost();

        double comboDiscount = 0;
        double bulkDiscount = 0;

        int bevCount = order.countCategoryItems("BEV");
        int foodCount = order.countCategoryItems("FOOD");

        if (bevCount >=1 && foodCount >= 2){comboDiscount = COMBO_DISCOUNT_RATE * orderPrice;}
        if (bevCount > 3){bulkDiscount = BULK_DISCOUNT_RATE * orderPrice;}
        double bestDiscount = Math.max(comboDiscount, bulkDiscount);
        return bestDiscount;
    }

    public double calculateTotalPrice(Order order){
        return order.getTotalCost() - calculateDiscount(order);
    }

    public String getAppliedRule(Order order){
        double rawTotal = order.getTotalCost();

        int bevCount  = order.countCategoryItems("BEV");
        int foodCount = order.countCategoryItems("FOOD");

        double comboDiscount   = (bevCount >= 1 && foodCount >= 2) ? rawTotal * COMBO_DISCOUNT_RATE : 0;
        double bulkDiscount    = (bevCount >= 3) ? rawTotal * BULK_DISCOUNT_RATE : 0;
        double bestDiscount = Math.max(comboDiscount, bulkDiscount);
        if (bestDiscount == comboDiscount) return "Combo Deal - 20% off";
        if (bestDiscount == bulkDiscount)  return "Bulk Drinks - 10% off";
        return "No discount";
    }
}
