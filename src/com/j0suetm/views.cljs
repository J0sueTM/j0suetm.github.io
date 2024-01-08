(ns com.j0suetm.views
  (:require
   [com.j0suetm.components.about :as cmp.about]
   [com.j0suetm.components.article-navbar :as cmp.article-navbar]
   [com.j0suetm.components.debug :as cmp.debug]
   [com.j0suetm.components.hero :as cmp.hero]
   [com.j0suetm.config :refer [config]]
   [helix.core :refer [$ <> defnc]]
   [helix.dom :as hd]))

(defnc home []
  (<>
   (hd/main
    {:class "w-full h-full max-screen-3xl mx-auto"}
    ($ cmp.article-navbar/article-navbar)
    (hd/div
     {:class (str "w-full h-full max-w-screen-xl mx-auto my-auto flex flex-col "
                  "font-cormorant font-medium text-2xl "
                  "lg:flex-row-reverse lg:w-full "
                  "xl:text-xl")}
     ($ cmp.hero/hero)
     ($ cmp.about/about)))
   (when (:debug config)
     ($ cmp.debug/debug))))
