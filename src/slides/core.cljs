(ns slides.core
  (:require [jobim.core :as jobim
             :refer [slide-show default-style ->Title
                     ->CaptionedPic ->ClojureCode ->Picture
                     ->Code ->Text code]
             :refer-macros [defshow defclj pseudo-clj pseudo-clj-points]]
            [cljs.test :refer-macros [deftest is testing run-tests]]
            [slides.components :as comps :refer [->Points]]))

(defshow om-next-client-side
         default-style
         (->Title
           "Om Next Client side" "Chris Murphy")
         (->CaptionedPic
           "/pictures/dog_food.jpg"
           "Om Next")
         (->CaptionedPic
           "/pictures/vacuum.jpg"
           "(Household) Components that suck")
         (->CaptionedPic
           "/pictures/Visicalc.png"
           "Spreadsheets - view is always 'live'")
         (->CaptionedPic
           "/pictures/calendar.jpg"
           "No events: actionListener, subscribe, reaction, bind...")
         (->Points ["component" "query" "query key(word)" "default db format" "ident" "ident keyword (by-id)"])
         (pseudo-clj-points 50 ["React component: props, children" "om dom namespaces"]
                            (defui AnyAction
                                   Object
                                   (render [this]
                                           (let [{:keys [text action]} (om/props this)]
                                             (dom/button #js{:onClick action} text)))))
         (pseudo-clj-points 50 ["factory method create instance" "calling"]
                            (def any-action (om/factory AnyAction))
                            (any-action {:text "Show State" :action #(pprint @my-reconciler)}))
         (pseudo-clj 80
                     [:id :line-name :selected?])
         (pseudo-clj 40
                     (defui Checkbox
                            static om/IQuery
                            (query [this]
                                   [:id :line-name :selected?])))
         (pseudo-clj-points 80 ["Joins - 1:M or 1:1"
                                "When running 0 -> 4 (incl) graph lines"
                                "All application data must be represented"
                                "Could have used Checkbox twice"
                                "Following all joins gives big query"
                                "Query is a tree"
                                "Where else :graph/graph-lines pops up..?"]
                            (defui Root
                                   static om/IQuery
                                   (query [this]
                                          [{:app/app-lines (om/get-query Checkbox)}
                                           {:graph/fake-graph (om/get-query FakeGraph)}
                                           {:graph/graph-lines (om/get-query Line)}
                                           ])))

         (pseudo-clj-points 70 ["Obviously the graph needs the lines!"
                                "An ident is made up of ident kw on left and id on right"
                                "Props are picked apart by render lifecycle method"
                                "Correspondence between query and props"
                                "In this case not to further component instances, despite join"]
                            (defui FakeGraph
                                   static om/Ident
                                   (ident [this props]
                                          [:fake-graph/by-id (:id props)])
                                   static om/IQuery
                                   (query [this]
                                          [:id
                                           {:graph/graph-lines (om/get-query Line)}])
                                   Object
                                   (render [this]
                                           (let [{:keys [graph/graph-lines]} (om/props this)]
                                             (dom/h2 #js{:className "fake-graph"} (apply str "GRAPH: " (interpose ", " (map :line-name graph-lines))))))))

         (pseudo-clj-points 60 ["In tree form, however not intended for components!"
                                "Written by hand for start-up state"
                                "Notice short cut of {:id 100} missing the rest of the query"
                                "Components with om/Ident needed for tree->db to work"]
                            (def init-state
                              {:graph/fake-graph {:id                1000
                                                  :graph/graph-lines [{:id 100}]}
                               :app/app-lines
                                                 [{:id        100
                                                   :line-name "Methane"
                                                   :selected? true}
                                                  {:id        101
                                                   :line-name "Oxygen"
                                                   :selected? false}
                                                  {:id        102
                                                   :line-name "Carbon Dioxide"
                                                   :selected? false}
                                                  {:id        103
                                                   :line-name "Carbon Monoxide"
                                                   :selected? false}
                                                  ]}))

         (pseudo-clj-points 60 ["Table and ref entries: get-in or get"
                                "This is default db format - fully normalized"
                                "No repeats except for the duplication I introduced"
                                "Ref entries for performance reasons"
                                "Above only seen when (pprint @my-reconciler)"]
                            {:graph/fake-graph [:fake-graph/by-id 1000],
                             :app/app-lines    [[:line/by-id 100]
                                                [:line/by-id 101]
                                                [:line/by-id 102]
                                                [:line/by-id 103]],
                             :line/by-id
                                               {100 {:id 100, :line-name "Methane", :selected? true},
                                                101 {:id 101, :line-name "Oxygen", :selected? false},
                                                102 {:id 102, :line-name "Carbon Dioxide", :selected? false},
                                                103 {:id 103, :line-name "Carbon Monoxide", :selected? false}},
                             :fake-graph/by-id
                                               {1000 {:id 1000, :graph/graph-lines [[:line/by-id 100]]}}})
         (->Points ["So far: html, css, query lang, nested components, data structure"
                    "i.e. Reading is mostly declarative"
                    "Mutations work on normalized data. Conceptually simple"])
         (pseudo-clj 80
                     (defmethod mutate 'graph/select-line
                       [{:keys [state]} _ params]
                       {:action #(swap! state update-lines params)}))
         (pseudo-clj 80
                     (defn update-lines
                       [last-state {:keys [want-to-select? id]}]
                       (-> last-state
                           (update-in [:line/by-id id] assoc :selected? want-to-select?)
                           (update-graph-lines want-to-select? id))))
         (pseudo-clj 80 (defn update-graph-lines [st want-to-select? id]
                          (let [graph-lines [:fake-graph/by-id 1000 :graph/graph-lines]]
                            (if want-to-select?
                              (update-in st graph-lines (fn [v] (vec (conj v [:line/by-id id]))))
                              (update-in st graph-lines (fn [v] (vec (remove #{[:line/by-id id]} v))))))))
         (->Points ["AST fiddling"
                    "set-query"
                    "IQueryParams"
                    "subquery"])
         (->CaptionedPic
           "/pictures/Saul-Goodman.jpeg"
           "Welcome back to the fat client"))
