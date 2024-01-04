(ns com.j0suetm.core
  (:require
   ["react" :as react]
   ["react-dom/client" :as react-dom-client]
   [cljs.pprint :refer [pprint]]
   [helix.core :refer [$ <> defnc]]
   [helix.dom :as hd]
   [helix.hooks :as hh]
   [refx.alpha :as refx]
   [refx.interceptors :as refx.itc]))

(enable-console-print!)

(goog-define DEBUG true)
(goog-define PROTOCOL "http")
(goog-define HOST "localhost")
(goog-define PORT "8200")
(def BASE_URL
  (str PROTOCOL "://" HOST
       (when PORT (str ":" PORT))))

(refx/reg-event-db
 :initialize-db
 (fn [_ _]
   {:articles {:raw [{:date 1617001106
                      :title "how to do something 1"
                      :author "j0suetm"}
                     {:date 1422134281
                      :title "Why to do another thing 1"
                      :author "j0suetm"}
                     {:date 1421160656
                      :title "Why is Jesus the only true way towards salvation"
                      :author "j0suetm"}
                     {:date 1617001106
                      :title "how to do something"
                      :author "j0suetm"}
                     {:date 1422134281
                      :title "Why to do another thing"
                      :author "j0suetm"}
                     {:date 1421160656
                      :title "Why is God the only true way towards salvation"
                      :author "j0suetm"}
                     {:date 1421160656
                      :title "How to master Clojure"
                      :author "j0suetm"}
                     {:date 1421160656
                      :title "Why is the Lord the only true way towards salvation Hello text overflow"
                      :author "j0suetm"}]
               :by-year (sorted-map)}
    :navbar {:open? false
             :search ""}}))

(refx/reg-sub
 :db
 (fn [db _] db))

;;--- articles
(refx/reg-sub
 :articles
 (fn [db _]
   (:articles db)))

(refx/reg-sub
 :raw-articles
 :<- [:articles]
 (fn [articles _]
   (:raw articles)))

(refx/reg-sub
 :articles-by-year
 :<- [:articles]
 (fn [articles _]
   (:by-year articles)))

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

;;--- navbar
(refx/reg-sub
 :navbar
 (fn [db _]
   (:navbar db)))

(refx/reg-sub
 :navbar-open?
 :<- [:navbar]
 (fn [navbar _]
   (:open? navbar)))

(refx/reg-event-db
 :toggle-navbar
 [(refx.itc/path :navbar)]
 (fn [navbar [_ new-stt]]
   (let [update-fn (if (some? new-stt)
                     (constantly new-stt)
                     not)]
     (update-in navbar [:open?] update-fn))))

(defnc social-links-cmp [{:keys [socials]}]
  (hd/div
   {:class "md:h-10 md:pl-2 md:flex md:flex-row md:items-center
            lg:pl-3"}
   (hd/div
    {:class "flex flex-row border-y border-black divide-x divide-black
             md:border-none md:divide-none"}
    (for [[link name] socials]
      (hd/a
       {:class "w-full py-2 text-center text-xl
                md:w-min md:px-2 md:py-0
                lg:px-3"
        :target "_blank"
        :href link}
       name)))
   (hd/div {:class "hidden w-full ml-2 border-t border-black
                    md:block
                    lg:ml-3"})
   (hd/button
    {:class "hidden w-fit link md:px-2 md-py-0 text-xl
             md:block"
     :on-click #(refx/dispatch-sync [:toggle-navbar true])}
    "Articles")))

(defnc about-header-cmp []
  (let [w-size (.. js/window -document -documentElement -clientWidth)
        sm? (< w-size 768)]
    (hd/header
     {:class "w-full px-4 py-4
              md:text-end
              lg:py-8"}
     (hd/h1
      {:class "font-abrilff font-bold text-4xl
               lg:text-5xl
               xl:text-6xl"}
      (str "Josué" (if sm? " T. " " Teodoro ") "Moreira"))
     (hd/p
      {:class "font-playfair font-medium text-xl
               md:text-2xl
               xl:mt-2 xl:text-3xl"}
      "@j0suetm | Software Engineer"))))

(defnc interests-col-cmp [{:keys [interests]}]
  (hd/ul
   {:class "list-disc list-inside"}
   (for [interest interests]
     (hd/li interest))))

(defnc interests-cmp [{:keys [left right]}]
  (hd/div
   {:class "w-full my-8 pl-2 flex flex-col
            lg:flex-row lg:space-x-6
            xl:my-4"}
   ($ interests-col-cmp {:interests left})
   ($ interests-col-cmp {:interests right})))

(defnc latest-articles-cmp [{:keys [qtt]}]
  (let [articles (refx/use-sub [:raw-articles])]
    (hd/ul
     {:class "mt-8 pl-2 list-disc list-inside
             xl:mt-4"}
     (for [{:keys [title date]} (take qtt articles)]
       (hd/li
        (hd/a
         {:target "_blank"
          :href (str BASE_URL "/articles/" title)}
         (let [human-date (-> (* date 1000)
                              js/Date.
                              (.toLocaleDateString "pt-BR"))]
           (str title " | " human-date))))))))

(defnc about-content-cmp []
  (hd/div
   {:class "px-4 py-6 whitespace-pre-line border-black
            xl:border-l-2"}
   "Greetings!\n\nI'm a brazilian 🇧🇷 Software Engineer, presently pursuing a "
   (hd/a {:target "_blank"
          :href "https://vtp.ifsp.edu.br/"} "Bachelor's degree")
   " and working on "
   (hd/a {:target "_blank"
          :href "https://moclojer.com"} "Moclojer.")
   " My primary interests include:"
   ($ interests-cmp {:left ["Functional Programming"
                            "Distributed Systems"
                            "Multimedia Processing"]
                     :right ["Clojure, ClojureScript"
                             "AWS, GCP, DevOps"
                             "FFmpeg, LibAV"]})
   "My latest articles:"
   ($ latest-articles-cmp {:qtt 2})
   (hd/button
    {:class "mt-8 link font-playfair text-md
             md:hidden"
     :on-click #(refx/dispatch-sync [:toggle-navbar true])}
    "All my articles ->")))

(defnc about-cmp []
  (hd/div
   {:class "w-full h-full xl:border-black
            lg:pt-6
            xl:border-b-2"}
   ($ about-header-cmp)
   ($ social-links-cmp {:socials [["https://github.com/j0suetm" "Github"]
                                  ["https://twitter.com/j0suetm" "Twitter/X"]
                                  ["https://linkedin.com/in/josue-teodoro-moreira"
                                   "LinkedIn"]]})
   ($ about-content-cmp)))

(defnc hero-cmp []
  (hd/div
   {:class "w-full h-40 border-b-2 border-black
            md:h-screen md:w-2/3 md:border-b-0 md:border-l-2
            xl:w-[32rem] xl:h-[38rem] xl:my-auto xl:mt-6 xl:border-2 xl:shadow-[0_100px_100px_-100px_rgba(3,4,4,1.0)]"}
   (hd/img
    {:class "w-full h-full object-cover object-[0,-100px]
             md:object-[-100px,0]
             xl:object-[0px,0px]"
     :src "resources/assets/img/hero.png"})))

(defnc article-by-year-list-cmp [{:keys [year articles]}]
  (hd/div
   {:class "w-full flex flex-col"}
   (hd/div
    {:class "flex flex-row font-playfair space-x-2 items-end"}
    (hd/p {:class "font-bold text-4xl"} year)
    (hd/p
     {:class "font-medium text-2xl"}
     (str ": " ((fnil count []) articles) " articles")))
   (hd/ul
    {:class "mt-4 space-y-2 list-disc list-inside"}
    (for [{:keys [title] :or {title "not-found"}} articles]
      (hd/li
       {:class "overflow-hidden"
        :key (str year "-" title)}
       (hd/a
        {:class (str "font-cormorant font-normal text-2xl text-black underline truncate "
                     "hover:bg-black hover:text-white")
         :href (str BASE_URL "/articles/" title)}
        title))))))

(defnc article-navbar-list-cmp []
  (let [raw-articles (refx/use-sub [:raw-articles])
        articles-by-year (refx/use-sub [:articles-by-year])]

    (hh/use-effect
      [raw-articles]
      (refx/dispatch-sync [:populate-articles-by-year]))

    (hd/div
     {:class "w-full px-4 py-2 flex flex-col space-y-4"}
     (for [[year articles] articles-by-year]
       ($ article-by-year-list-cmp {:year year
                                    :articles articles})))))

(defnc close-button-cmp [{:keys [click-fn]}]
  (hd/button
   {:class ""
    :on-click click-fn}
   (hd/svg
    {:class "mt-2"
     :width "28" :height "28"
     :view-box "0 0 16 16"}
    (hd/path {:d (str "M2.146 2.854a.5.5 0 1 1 .708-.708L8 7.293l5.146-5.147a.5.5 0 0 1 "
                      ".708.708L8.707 8l5.147 5.146a.5.5 0 0 1-.708.708L8 8.707l-5.146 "
                      "5.147a.5.5 0 0 1-.708-.708L7.293 8z")}))))

(defnc article-navbar-cmp []
  (let [navbar-open? (refx/use-sub [:navbar-open?])
        close-navbar-fn #(refx/dispatch-sync [:toggle-navbar false])
        [search set-search!] (hh/use-state "")]

    (hh/use-effect
      [search]
      (refx/dispatch-sync [:populate-articles-by-year search]))

    (hd/div
     {:class (str "w-full h-full flex flex-row "
                  (if navbar-open? "fixed" "hidden"))}
     (hd/div
      {:class "w-full bg-white overflow-hidden md:w-1/2 xl:w-1/3"}
      ;; header
      (hd/div
       {:class "w-full mb-8 px-4 pt-4 flex flex-row items-center justify-between"}
       (hd/p {:class "font-playfair font-bold text-4xl"} "All my articles")
       ($ close-button-cmp {:click-fn close-navbar-fn}))
      (hd/input
       {:class "w-full p-4 border-2 border-y-black font-cormorant text-xl"
        :placeholder "Search..."
        :type "text"
        :on-change #(set-search! (.. % -target -value))})
      ($ article-navbar-list-cmp))
     (hd/div {:class "hidden w-full h-full bg-black opacity-50
                      md:block md:w-1/2 xl:w-2/3"
              :on-click close-navbar-fn}))))

(defnc debug-cmp []
  (let [db (refx/use-sub [:db])]
    (hd/pre (with-out-str (pprint db)))))

(defnc home-view []
  (<>
   (hd/main
    {:class "w-full h-full max-screen-3xl mx-auto"}
    ($ article-navbar-cmp)
    (hd/div
     {:class (str "w-full h-full max-w-screen-xl mx-auto my-auto flex flex-col "
                  "font-cormorant font-medium text-2xl "
                  "md:flex-row-reverse md:w-full "
                  "xl:text-xl")}
     ($ hero-cmp)
     ($ about-cmp)))
   (when DEBUG ($ debug-cmp))))

(defonce app-root
  (react-dom-client/createRoot (.getElementById js/document "app")))

(defn render []
  (.render app-root ($ react/StrictMode ($ home-view))))

(defn ^:dev/after-load clear-cache-and-render! []
  (refx/clear-subscription-cache!)
  (render))

(defn ^:export init []
  (refx/dispatch [:initialize-db])
  (render))
