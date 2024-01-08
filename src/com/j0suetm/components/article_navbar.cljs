(ns com.j0suetm.components.article-navbar
  (:require
   [com.j0suetm.config :refer [config]]
   [helix.core :refer [$ defnc]]
   [helix.dom :as hd]
   [helix.hooks :as hh]
   [refx.alpha :as refx]))

(defnc close-button [{:keys [click-fn]}]
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

(defnc article-by-year-list [{:keys [year articles]}]
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
         :href (str (:base-url config) "/articles/" title)}
        title))))))

(defnc article-list []
  (let [raw-articles (refx/use-sub [:raw-articles])
        articles-by-year (refx/use-sub [:articles-by-year])]

    (hh/use-effect
      [raw-articles]
      (refx/dispatch-sync [:populate-articles-by-year]))

    (hd/div
     {:class "w-full px-4 py-2 flex flex-col space-y-4"}
     (for [[year articles] articles-by-year]
       ($ article-by-year-list {:year year
                                :articles articles})))))

(defnc article-navbar []
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
       ($ close-button {:click-fn close-navbar-fn}))
      (hd/input
       {:class "w-full p-4 border-2 border-y-black font-cormorant text-xl"
        :placeholder "Search..."
        :type "text"
        :on-change #(set-search! (.. % -target -value))})
      ($ article-list))
     (hd/div {:class "hidden w-full h-full bg-black opacity-50
                      md:block md:w-1/2 xl:w-2/3"
              :on-click close-navbar-fn}))))
