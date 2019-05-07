package model;

import controllers.Merchant;

import java.util.HashMap;
import java.util.PriorityQueue;

public class MerchantModel {

    public HashMap<String, Merchant> merchantMap = new HashMap<>();
    public PriorityQueue<Merchant> merchantMaxHeap = new PriorityQueue<>((first, second) -> {
        return second.getVisitedCount() - first.getVisitedCount();
    });
}
