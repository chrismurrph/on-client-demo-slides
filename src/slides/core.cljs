(ns slides.core
  (:require [jobim.core :as jobim
             :refer [slide-show default-style ->Title
                     ->CaptionedPic ->ClojureCode ->Picture
                     ->Code ->Text code]
             :refer-macros [defshow defclj pseudo-clj pseudo-clj-points]]
            [cljs.test :refer-macros [deftest is testing run-tests]]
            [slides.components :as comps :refer [->Points]]))

(defshow advent
         default-style
         (->Title
           "Advent of Code" "Chris Murphy")
         (->Points ["read-string"
                    "re-find, re-matches, re-seq"
                    "partition, partition-all, partition-by"
                    "iterate"
                    "reductions"
                    "take-while, drop-while"
                    "Canonical"])
         (pseudo-clj-points 70
                            ["R2, L1, R2, R1, R1, L3, R3, L5,"]
                     (def data
                       (->> (read-string (str "[" (slurp (io/resource "one.txt")) "]"))
                            (map name))))
         (pseudo-clj-points 70
                            ["\"bot 75 gives low to bot 145 and high to bot 95\""
                             "([\"bot 75\" \"bot\" \"75\"] [\"bot 145\" \"bot\" \"145\"] [\"bot 95\" \"bot\" \"95\"])"
                             "(:bot75 :bot145 :bot95)"
                             ""
                             "\"bot 96 gives low to output 10 and high to bot 66\""
                             "(:bot96 :output10 :bot66)"]
                            (defn addr-names [l]
                              (->> (re-seq #"(bot|output)\s(\d+)" l)
                                   (map (comp keyword #(apply str %) rest)))))
         )
