(ns com.j0suetm.events
  (:require
   [refx.alpha :as refx]
   [refx.interceptors :as refx.itc]))

(refx/reg-event-db
 :initialize-db
 (fn [_ _]
   {:articles {:raw []
               :by-year (sorted-map)}
    :navbar {:open? false
             :search ""}}))

(defn group-articles-by-year [articles]
  (reduce
   (fn [grouped-articles article]
     (let [year (-> (:date article)
                    (* 1000)
                    js/Date.
                    .getUTCFullYear)
           new-articles (-> (get grouped-articles year)
                            (conj article)
                            vec)]
       (->> (assoc grouped-articles year new-articles)
            (into (sorted-map-by >)))))
   {} articles))

(defn filter-raw-articles [articles input]
  (if (empty? input)
    articles
    (filter (fn [{:keys [title]}]
              (.includes (.toLowerCase title)
                         (.toLowerCase input)))
            articles)))

(refx/reg-event-db
 :populate-articles-by-year
 (fn [db [_ search-filter]]
   (assoc-in
    db [:articles :by-year]
    (-> (get-in db [:articles :raw])
        (filter-raw-articles search-filter)
        group-articles-by-year))))

(refx/reg-event-db
 :toggle-navbar
 [(refx.itc/path :navbar)]
 (fn [navbar [_ new-stt]]
   (let [update-fn (if (some? new-stt)
                     (constantly new-stt)
                     not)]
     (update-in navbar [:open?] update-fn))))
