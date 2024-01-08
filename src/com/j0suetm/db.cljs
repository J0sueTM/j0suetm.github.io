(ns com.j0suetm.db
  (:require
   [refx.alpha :as refx]))

(refx/reg-sub
 :db
 (fn [db _] db))

(refx/reg-sub
 :articles
 (fn [db _]
   (:articles db)))

(refx/reg-sub
 :raw-articles
 :<- [:article]
 (fn [articles _]
   (:raw articles)))

(refx/reg-sub
 :articles-by-year
 :<- [:articles]
 (fn [articles _]
   (:by-year articles)))

(refx/reg-sub
 :navbar
 (fn [db _]
   (:navbar db)))

(refx/reg-sub
 :navbar-open?
 :<- [:navbar]
 (fn [navbar _]
   (:open? navbar)))
