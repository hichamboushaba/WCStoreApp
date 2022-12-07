//
//  ProductsList.swift
//  WCStoreApp
//
//  Created by Hicham Boushaba on 10/3/2022.
//

import SwiftUI
import Combine
import WCStoreAppKmm
import KMPNativeCoroutinesCombine

class HomeViewModelWrapper: ViewModelWrapper<HomeViewModel> {
    var productsPagingObservable: PagingObservable<ProductUiModel>!

    init() {
        super.init()
        productsPagingObservable = PagingObservable(flow: viewModel.products)
    }
}

struct HomeScreen: View {
    @StateObject private var viewModelWrapper = HomeViewModelWrapper()
    
    private var viewModel: HomeViewModel {
        viewModelWrapper.viewModel
    }
    
    var body: some View {
        Screen(hasNavigationBar: false) {
            ProductsList(
                productsPagingObservable: viewModelWrapper.productsPagingObservable,
                onProductClick: viewModel.onProductClicked
            )
        }
        .effects(viewModel: viewModel)
    }
}
