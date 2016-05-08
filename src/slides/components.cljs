(ns slides.components
  (:require [jobim.core :as jobim :refer [Slide]]))

(defn left [pct elem]
  [:div {:style {:width       (str pct "%")
                 :margin-left "10px"
                 ;:margin-right "auto"
                 }}
   elem])

(def list-item-style {:margin-top "10px"
                      :margin-bottom "10px"})

;; Modified from Text
(defrecord SmallerText [text]
  Slide
  (render-slide [this]
    [:div (comment {:style jobim/title-style})
     (left 500
           [:h2 {:style jobim/h2-style} text])])
  (jobim/next-slide [this state] (jobim/std-next this state))
  (jobim/prev-slide [this state] (jobim/std-prev this state)))

;; Custom
(defrecord Points [items]
  Slide
  (render-slide [this]
    [:div nil
     (left 100
           [:h2 {:style jobim/h2-style} (into [:ul] (map (fn [item] [:li {:style list-item-style} item]) items))])])
  (jobim/next-slide [this state] (jobim/std-next this state))
  (jobim/prev-slide [this state] (jobim/std-prev this state)))
