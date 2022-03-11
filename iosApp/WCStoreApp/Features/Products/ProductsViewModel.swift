//
//  ProductsViewModel.swift
//  WCStoreApp
//
//  Created by Hicham Boushaba on 10/3/2022.
//

import Foundation
import WCStoreAppKmm

class ProductsViewModel: ObservableObject {
    @Published public var products: [Product] = []
    let repository:ProductsRepository
    let pager:PagingPager<KotlinInt, Product>
    
    init() {
        repository = KoinWrapper.shared.get(objCProtocol:ProductsRepository.self) as! ProductsRepository
        pager = NetworkProductsKt.getProductsList(repository)
        
        FlowUtilsKt.wrap(pager.pagingData).watch(block: { nullableArray in
            guard let list = nullableArray as? Array<Product> else {
                return
            }
            
            self.products = list
        })
    }
        
    func fetchNextData() {
        pager.loadNext()
    }
    
    public var shouldDisplayNextPage: Bool {
        return pager.hasNextPage
    }
}
