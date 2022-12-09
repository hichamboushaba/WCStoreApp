//
//  ProductsList.swift
//  WCStoreApp
//
//  Created by Hicham on 13/9/2022.
//

import SwiftUI
import WCStoreAppKmm

struct ProductsList: View {
    @ObservedObject var productsPagingObservable: PagingObservable<ProductUiModel>
    let onProductClick: (Product) -> Void

    var body: some View {
    let array = (0..<productsPagingObservable.count).map { index in
        productsPagingObservable.peek(position: index)
    }.filter{ item in item != nil }

    let arrayIndexed = array.enumerated().map({ $0 })

    ScrollView {
        LazyVStack {
            ForEach(arrayIndexed, id: \.element?.product.id) { index, _ in
                let productUiModel = productsPagingObservable.get(position: index)
                if (productUiModel != nil) {
                    ProductsListRowView(
                            uiModel: productUiModel!,
                            onClick: {
                                onProductClick(productUiModel!.product)
                            }
                    )
                            .buttonStyle(BorderlessButtonStyle())
                            .padding([.leading, .trailing])
                    Divider()
                }
            }
        }
        if !productsPagingObservable.loadStates.append.endOfPaginationReached {
            nextPageView
        }
        }
                .padding(.top)
    }

    private var nextPageView: some View {
        HStack {
            Spacer()
            VStack {
                ProgressView()
            }
            Spacer()
        }
    }
}

private struct ProductsListRowView: View {
    let uiModel: ProductUiModel
    let onClick: () -> Void

    var body: some View {
        HStack {
            if let image = uiModel.product.images.first,
               let url = URL(string: image) {
                AsyncImage(url: url) { image in
                    image.resizable()
                } placeholder: {
                    ProgressView()
                }
                        .frame(width: 50, height: 50)
                        .clipShape(RoundedRectangle(cornerRadius: 8))
            } else {
                RoundedRectangle(cornerRadius: 8)
                        .frame(width: 50, height: 50)
                        .foregroundColor(.gray)
            }
            VStack(alignment: .leading) {
                Text(uiModel.product.name)
                        .font(.title3)
                        .foregroundColor(.accentColor)
                Text(uiModel.priceFormatted)
                        .font(.footnote)
                        .foregroundColor(.gray)
            }
                    .frame(maxWidth: .infinity, alignment: .leading)

            CartStepper(
                    count: Int(uiModel.quantityInCart),
                    addClick: {
                        uiModel.addToCart()
                    },
                    deleteClick: {
                        uiModel.deleteFromCart()
                    }
            )
        }
                .onTapGesture(perform: onClick)
    }
}
